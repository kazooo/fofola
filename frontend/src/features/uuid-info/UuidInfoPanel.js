import {Panel} from "../../components/container/Panel";
import {Table} from "../../components/container/Table";
import {useSelector} from "react-redux";
import {getUuidInfo} from "./slice";
import {UuidInfoRow} from "./UuidInfoRow";

export const UuidInfoPanel = () => {

    const uuidInfo = useSelector(state => getUuidInfo(state));
    const header = ["Uuid", "Model", "Uložený v Solru / Fedoře",
        "Viditelnost v Solru / Fedoře", "Odkaz na obrázek",
        "Název dokumentu", "Poslední modifikace", "Operace"];

    return <Panel>
        <Table header={header}>
            {uuidInfo.map(info => <UuidInfoRow data={info} />)}
        </Table>
    </Panel>
};
