import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {LinkDonatorForm} from "./LinkDonatorForm";
import {LinkDonatorPanel} from "./LinkDonatorPanel";

export const LinkDonator = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <LinkDonatorForm />
                <LinkDonatorPanel />
            </ContainerHeader>
        </Container>
    </div>
);
