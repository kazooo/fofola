import {Navbar} from "../../components/navbar/Navbar";
import {Container} from "../../components/container/Container";
import {ContainerHeader} from "../../components/container/ContainerHeader";
import {SetImageForm} from "./SetImageForm";
import {SetImgPanel} from "./SetImgPanel";

export const SetImage = () => (
    <div>
        <Navbar />
        <Container>
            <ContainerHeader>
                <SetImageForm />
                <SetImgPanel />
            </ContainerHeader>
        </Container>
    </div>
);
