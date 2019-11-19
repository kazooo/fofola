package cz.mzk.fofola.threads;

import cz.mzk.fofola.model.FofolaProcess;
import cz.mzk.fofola.model.SolrDocument;
import cz.mzk.fofola.service.SolrCommunicator;
import cz.mzk.fofola.service.XMLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.Cursor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SitemapGenerationThread extends FofolaThread {

    private final SolrCommunicator solrCommunicator;
    private final XMLService xmlService;
    private String collectionName;
    private String outDirPath;
    private long sitemapCounter;
    private long total;
    private final static int maxDocPerSitemap = 50_000;
    private final static int maxSitemaps = 50_000;
    private final static int docPerQuery = 5000;
    private static SimpleQuery query;
    private final static SimpleDateFormat sitemapDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final static Logger logger = LoggerFactory.getLogger(SitemapGenerationThread.class);

    private static final String locElementName = "loc";
    private static final String xmlnsAttrName = "xmlns";
    private static final String lastModElementName = "lastmod";
    private static final String sitemapElementName = "sitemap";
    private static final String sitemapIndexElementName = "sitemapindex";
    private final static String digitalniKnihovnaUrl = "http://www.digitalniknihovna.cz";
    private static final String xmlnsAttrValue = "http://www.sitemaps.org/schemas/sitemap/0.9";

    private static final String urlElementName = "url";
    private static final String urlSetElementName = "urlset";

    public SitemapGenerationThread(SolrCommunicator solrCommunicator,
                                   FofolaThreadEventPublisher eventPublisher,
                                   XMLService xmlService) {
        super(eventPublisher);
        super.setType(FofolaProcess.GENERATE_SITEMAP_TYPE);
        this.solrCommunicator = solrCommunicator;
        this.xmlService = xmlService;

        query = new SimpleQuery("details:/.*TitlePage.*/");
        query.addSort(new Sort(Sort.Direction.ASC, SolrDocument.ID));
        query.addProjectionOnFields(SolrDocument.PARENT_PID, SolrDocument.MODIFIED_DATE);
        query.setRows(docPerQuery);
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setOutputDir(String outPath) {
        this.outDirPath = outPath;
    }

    public long getDone() {
        return sitemapCounter;
    }

    public long getTotal() {
        return total;
    }

    @Override
    protected void process() throws Exception {
        sitemapCounter = 0;
        total = solrCommunicator.docCount(query);

        String folderName = outDirPath + "/";
        String sitemapSuffix = ".xml";
        String sitemapNamePrefix = "sitemap_";

        Document sitemapIndex = xmlService.newDoc();
        Element sitemapIndexElement = sitemapIndex.createElement(sitemapIndexElementName);
        sitemapIndex.appendChild(sitemapIndexElement);

        Attr attr = sitemapIndex.createAttribute(xmlnsAttrName);
        attr.setValue(xmlnsAttrValue);
        sitemapIndexElement.setAttributeNode(attr);

        Cursor<SolrDocument> solrDocs = solrCommunicator.getDocCursor(collectionName, query);

        while (sitemapCounter < maxSitemaps) {
            String outFileName = sitemapNamePrefix + sitemapCounter + sitemapSuffix;
            String path = folderName + "/" + outFileName;

            boolean generated = generateSitemap(solrDocs, path, maxDocPerSitemap);
            if (!generated) {
                break; // no more uuids
            }

            Element sitemap = xmlService.createElement(sitemapIndex, sitemapIndexElement, sitemapElementName);
            Element loc = xmlService.createElement(sitemapIndex, sitemap, locElementName);

            xmlService.setTextNode(sitemapIndex, loc, generateSitemapLocUrl(outFileName));

            Element lastMod = xmlService.createElement(sitemapIndex, sitemap, lastModElementName);
            xmlService.setTextNode(sitemapIndex, lastMod, sitemapDateFormat.format(new Date()));
            sitemapCounter++;
        }

        if (sitemapCounter != 0) {
            xmlService.saveDoc(sitemapIndex, outDirPath + "/sitemap_index.xml");
        }
    }

    private boolean generateSitemap(Cursor<SolrDocument> solrDocs,
                                    String outFileName, int maxDocs) throws TransformerException {
        int done = 0;

        Document sitemap = xmlService.newDoc();
        Element urlSet = sitemap.createElement(urlSetElementName);
        sitemap.appendChild(urlSet);

        Attr attr = sitemap.createAttribute(xmlnsAttrName);
        attr.setValue(xmlnsAttrValue);
        urlSet.setAttributeNode(attr);

        while (done < maxDocs && solrDocs.hasNext()) {

            SolrDocument solrDoc = solrDocs.next();

            Element url = xmlService.createElement(sitemap, urlSet, urlElementName);
            Element loc = xmlService.createElement(sitemap, url, locElementName);

            final String parentPid = solrDoc.getParentPids().get(0);
            xmlService.setTextNode(sitemap, loc, generateViewLocUrl(parentPid));

            final String modDate = solrDoc.getModifiedDate();
            if (modDate != null) {
                Element lastMod = xmlService.createElement(sitemap, url, lastModElementName);
                xmlService.setTextNode(sitemap, lastMod, modDate);
            }

            done++;
        }

        if (done != 0) {
            xmlService.saveDoc(sitemap, outFileName);
        }
        return done != 0;
    }

    private String generateSitemapLocUrl(String sitemapName) {
        return digitalniKnihovnaUrl + "/" + sitemapName;
    }

    private String generateViewLocUrl(String uuid) {
        return digitalniKnihovnaUrl + "/mzk/view/" + uuid;
    }
}

