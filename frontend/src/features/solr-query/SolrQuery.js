import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {SolrQueryForm} from "./SolrQueryForm";
import {FileTable} from "./FileTable";

export const SolrQuery = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <SolrQueryForm />
            </ContainerHeader>
            <FileTable />
        </Container>
    </div>
);
