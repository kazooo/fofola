// Insert Thymeleaf values
/*<![CDATA[*/
var tree_data = /*[[${tree_data}]]*/ 'awesome_tree';
/*]]>*/

var tree_container = document.getElementById('tree_container');
var width = tree_container.offsetWidth;
var height = tree_container.offsetHeight;
var margin = {top: 0, right: 0, bottom: 0, left: 0};

// Misc. variables
var i = 0;
var duration = 750;
var root;

var tree = d3.layout.tree()
    .size([height, width]);

// define the zoomListener which calls the zoom function on the "zoom" event constrained within the scaleExtents
var zoomListener = d3.behavior.zoom().scaleExtent([0.1, 3]).on("zoom", zoom);

// Define the zoom function for the zoomable tree
var svg = d3.select("#tree_container").append("svg")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .call(zoomListener)
    .append("g")
    .attr("transform", "translate(" + width/2 + "," + 0 + ")");

function zoom() {
    svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
}

var diagonal = d3.svg.diagonal().projection(function(d) { return [d.y, d.x]; });

function init(data) {
    root = JSON.parse(data);
    root.x0 = height / 2;
    root.y0 = 0;
    function collapse(d) {
        if (d.children) {
            d._children = d.children;
            d._children.forEach(function(d1){d1.parent = d; collapse(d1);});
            d.children = null;
        }
    }

    function find(d, name) {
        if (d.name == name){
            while(d.parent){
                d = d.parent;
                click(d)
            }
            return;
        }
        if (d.children) {
            d.children.forEach(function(d){find(d, name)});
        } else if(d._children){
            d._children.forEach(function(d){find(d, name)});
        }
    }

    [root].forEach(collapse);
    var name = "layout";
    find (root, name);
    update(root);
    centerNode(root);
}

init(tree_data);

d3.select(self.frameElement).style("height", width/2);

// Function to center node when clicked/dropped so node doesn't get lost when collapsing/moving with large amount of children.
function centerNode(source) {
    scale = zoomListener.scale();
    x = -source.y0;
    y = -source.x0;
    x = x * scale + width / 2;
    y = y * scale + height / 2;
    d3.select('g').transition()
        .duration(duration)
        .attr("transform", "translate(" + x + "," + y + ")scale(" + scale + ")");
    zoomListener.scale(scale);
    zoomListener.translate([x, y]);
}

function update(source) {
    // Compute the new height, function counts total children of root node and sets tree height accordingly.
    // This prevents the layout looking squashed when new nodes are made visible or looking sparse when nodes are removed
    // This makes the layout more consistent.
    var levelWidth = [1];
    var childCount = function(level, n) {
        if (n.children && n.children.length > 0) {
            if (levelWidth.length <= level + 1) levelWidth.push(0);

            levelWidth[level + 1] += n.children.length;
            n.children.forEach(function(d) {
                childCount(level + 1, d);
            });
        }
    };
    childCount(0, root);
    var newHeight = d3.max(levelWidth) * 25; // 25 pixels per line
    tree = tree.size([newHeight, width]);

    // Compute the new tree layout.
    var nodes = tree.nodes(root).reverse(),
        links = tree.links(nodes);

    // Normalize for fixed-depth.
    nodes.forEach(function(d) { d.y = d.depth * 300; });

    // Update the nodes…
    var node = svg.selectAll("g.node")
        .data(nodes, function(d) { return d.id || (d.id = ++i); });

    // Enter any new nodes at the parent's previous position.
    var nodeEnter = node.enter().append("g")
        .attr("class", "node")
        .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
        .on("click", leftClick)
        .on("contextmenu", function (d, i) {
            d3.event.preventDefault();
            rightClick(d)
        });

    nodeEnter.append("circle")
        .attr("r", 1e-6)
        .style("fill", checkNodeReturnColor)
        .style("stroke", checkChildsReturnColor);
    // function(d) { return d._children ? "lightsteelblue" : "#fff"; });

    nodeEnter.append("text")
        .attr("x", function(d) { return d.children || d._children ? -10 : 10; })
        .attr("dy", ".35em")
        .attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
        .text(function(d) { return d.name; })
        .style("fill-opacity", 1e-6);

    // Transition nodes to their new position.
    var nodeUpdate = node.transition()
        .duration(duration)
        .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; });

    nodeUpdate.select("circle")
        .attr("r", 4.5)
        .style("fill", checkNodeReturnColor)
        .style("stroke", checkChildsReturnColor);
    // function(d) { return d._children ? "lightsteelblue" : "#fff"; }

    nodeUpdate.select("text")
        .style("fill-opacity", 1);

    // Transition exiting nodes to the parent's new position.
    var nodeExit = node.exit().transition()
        .duration(duration)
        .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
        .remove();

    nodeExit.select("circle")
        .attr("r", 1e-6);

    nodeExit.select("text")
        .style("fill-opacity", 1e-6);

    // Update the links…
    var link = svg.selectAll("path.link")
        .data(links, function(d) { return d.target.id; });

    // Enter any new links at the parent's previous position.
    link.enter().insert("path", "g")
        .attr("class", function (d) {
            var set_red_color = source.hasProblem || source.hasProblematicChild;
            return set_red_color ? "link error" : "link";
        })
        .attr("d", function(d) {
            var o = {x: source.x0, y: source.y0};
            return diagonal({source: o, target: o});
        });

    // Transition links to their new position.
    link.transition()
        .duration(duration)
        .attr("d", diagonal);

    // Transition exiting nodes to the parent's new position.
    link.exit().transition()
        .duration(duration)
        .attr("d", function(d) {
            var o = {x: source.x, y: source.y};
            return diagonal({source: o, target: o});
        })
        .remove();

    // Stash the old positions for transition.
    nodes.forEach(function(d) {
        d.x0 = d.x;
        d.y0 = d.y;
    });
}

// Toggle children on rightClick.
function rightClick(d) {
    if (d.children) {
        d._children = d.children;
        d.children = null;
    } else {
        d.children = d._children;
        d._children = null;
    }
    update(d);
    centerNode(d);
}

function leftClick(d) {
    document.getElementById('info_container').style.visibility = 'visible';
    document.getElementsByTagName("span").namedItem("uuid").textContent = d.uuid;
    document.getElementsByTagName("span").namedItem("model").textContent = d.model;
    document.getElementsByTagName("span").namedItem("vis_solr").textContent = d.visibilitySolr;
    document.getElementsByTagName("span").namedItem("vis_fedora").textContent = d.visibilityFedora;

    document.getElementsByTagName("div").namedItem("indexed").className = d.indexed === "true" ? 'ok' : 'error';
    document.getElementsByTagName("div").namedItem("stored").className = d.stored === "true" ? 'ok' : 'error';

    var hasNoImage = d.imageUrl === "no image";

    // TODO disgusting...
    if (hasNoImage) {
        document.getElementsByTagName("a").namedItem("img_link").className = "disabled";
        document.getElementsByTagName("a").namedItem("img_link").textContent = "no image";
    } else {
        document.getElementsByTagName("a").namedItem("img_link").className = "";
        document.getElementsByTagName("a").namedItem("img_link").href = d.imageUrl;
        document.getElementsByTagName("a").namedItem("img_link").textContent = "image link";
    }

    centerNode(d);
}

function checkNodeReturnColor(d) {
    return d.hasProblem ? "red" : "green";
}

function checkChildsReturnColor(d) {
    return d.hasProblematicChild ? "red" : "green";
}