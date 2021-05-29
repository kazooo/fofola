import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {Header} from "./Header";
import {FileTable} from "./FileTable";

export const CheckDonator = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <Header />
            </ContainerHeader>
            <FileTable />
        </Container>
    </div>
);
