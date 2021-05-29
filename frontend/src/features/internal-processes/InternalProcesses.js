import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {Header} from "./Header";
import {ProcessTable} from "./ProcessTable";

export const InternalProcesses = () => (
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
