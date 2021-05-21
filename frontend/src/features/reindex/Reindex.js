import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {ReindexForm} from "./ReindexForm";
import {ReindexPanel} from "./ReindexPanel";

export const Reindex = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <ReindexForm />
                <ReindexPanel />
            </ContainerHeader>
        </Container>
    </div>
);
