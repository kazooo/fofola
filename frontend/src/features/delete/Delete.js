import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {DeleteForm} from "./DeleteForm";
import {DeletePanel} from "./DeletePanel";

export const Delete = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <DeleteForm />
                <DeletePanel />
            </ContainerHeader>
        </Container>
    </div>
);
