import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {PdfForm} from "./PdfForm";
import {FileTable} from "./FileTable";
import {PdfPanel} from "./PdfPanel";

export const Pdf = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <PdfForm />
                <PdfPanel />
            </ContainerHeader>
            <FileTable />
        </Container>
    </div>
);
