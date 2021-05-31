import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {PerioPartsPublishForm} from "./PerioPartsPublishForm";
import {PerioPartsPublishPanel} from "./PerioPartsPublishPanel";

export const PerioPartsPublish = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <PerioPartsPublishForm />
            </ContainerHeader>
            <PerioPartsPublishPanel />
        </Container>
    </div>
);
