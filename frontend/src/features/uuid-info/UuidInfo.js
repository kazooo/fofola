import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {UuidInfoForm} from "./UuidInfoForm";
import {UuidInfoPanel} from "./UuidInfoPanel";

export const UuidInfo = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <UuidInfoForm />
                <UuidInfoPanel />
            </ContainerHeader>
        </Container>
    </div>
);
