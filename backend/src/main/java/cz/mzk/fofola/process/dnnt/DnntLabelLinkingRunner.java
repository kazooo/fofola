package cz.mzk.fofola.process.dnnt;

public interface DnntLabelLinkingRunner {
    void run(String uuid) throws Exception;
    void commitAndClose();
}
