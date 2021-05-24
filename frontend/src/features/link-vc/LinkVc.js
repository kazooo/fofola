import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {LinkVcForm} from "./LinkVcForm";
import {LinkVcPanel} from "./LinkVcPanel";

export const LinkVc = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <LinkVcForm />
                <LinkVcPanel />
            </ContainerHeader>
        </Container>
    </div>
);
