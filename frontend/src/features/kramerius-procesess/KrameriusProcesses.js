import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {Header} from "./Header";
import {ProcessTable} from "./ProcessTable";
import {Navbar} from "../../components/navbar/Navbar";

export const KrameriusProcesses = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <Header />
            </ContainerHeader>
            <ProcessTable />
        </Container>
    </div>
);
