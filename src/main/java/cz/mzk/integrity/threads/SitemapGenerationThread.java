package cz.mzk.integrity.threads;

import cz.mzk.integrity.model.FofolaProcess;
import cz.mzk.integrity.model.SolrDocument;
import cz.mzk.integrity.service.SolrCommunicator;
import cz.mzk.integrity.service.XMLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private final static int docPerQuery = 1000;
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

        String folderName = outDirPath + "/maps/";
        String sitemapSuffix = ".xml";
        String sitemapNamePrefix = "sitemap_";

        Document sitemapIndex = xmlService.newDoc();
        Element sitemapIndexElement = sitemapIndex.createElement(sitemapIndexElementName);
        sitemapIndex.appendChild(sitemapIndexElement);

        Attr attr = sitemapIndex.createAttribute(xmlnsAttrName);
        attr.setValue(xmlnsAttrValue);
        sitemapIndexElement.setAttributeNode(attr);

        while (sitemapCounter < maxSitemaps) {
            String outFileName = sitemapNamePrefix + sitemapCounter + sitemapSuffix;
            String path = folderName + "/" + outFileName;

            boolean generated = generateSitemap(path, maxDocPerSitemap);
            if (!generated) {
                break;
            }

            Element sitemap = xmlService.createElement(sitemapIndex, sitemapIndexElement, sitemapElementName);
            Element loc = xmlService.createElement(sitemapIndex, sitemap, locElementName);

            xmlService.setTextNode(sitemapIndex, loc, generateLocUrl(outFileName));

            Element lastMod = xmlService.createElement(sitemapIndex, sitemap, lastModElementName);
            xmlService.setTextNode(sitemapIndex, lastMod, sitemapDateFormat.format(new Date()));
            sitemapCounter++;
        }

        xmlService.saveDoc(sitemapIndex, outDirPath + "/sitemap_index.xml");
    }

    private boolean generateSitemap(String outFileName, int maxDocs) throws TransformerException {
        int done = 0;

        Document sitemap = xmlService.newDoc();
        Element urlSet = sitemap.createElement(urlSetElementName);
        sitemap.appendChild(urlSet);

        Attr attr = sitemap.createAttribute(xmlnsAttrName);
        attr.setValue(xmlnsAttrValue);
        urlSet.setAttributeNode(attr);

        while (done < maxDocs) {
            query.setRows(batchSize(docPerQuery, done, maxDocs));
            List<SolrDocument> solrDocs = solrCommunicator.cursorQuery(collectionName, query);

            for (SolrDocument solrDoc : solrDocs) {
                Element url = xmlService.createElement(sitemap, urlSet, urlElementName);
                Element loc = xmlService.createElement(sitemap, url, locElementName);

                final String parentPid = solrDoc.getParentPids().get(0);
                xmlService.setTextNode(sitemap, loc, generateLocUrl(parentPid));

                final Date modDate = solrDoc.getModifiedDate();
                if (modDate != null) {
                    Element lastMod = xmlService.createElement(sitemap, url, lastModElementName);
                    xmlService.setTextNode(sitemap, lastMod, sitemapDateFormat.format(modDate));
                }
            }

            done += solrDocs.size();
        }

        logger.info("generated");
        xmlService.saveDoc(sitemap, outFileName);
        return done != 0;
    }

    private int batchSize(int docPerQuery, int done, int max) {
        return Math.min(docPerQuery, (max - done));
    }

    private String generateLocUrl(String sitemapName) {
        return digitalniKnihovnaUrl + "/" + sitemapName;
    }
}

