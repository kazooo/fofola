import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {ChangeAccessForm} from "./ChangeAccessForm";
import {ChangeAccessPanel} from "./ChangeAccessPanel";

export const ChangeAccess = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <ChangeAccessForm />
                <ChangeAccessPanel />
            </ContainerHeader>
        </Container>
    </div>
);
