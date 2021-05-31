import {Panel} from "../../components/container/Panel";
import {TextForm} from "../../components/form/TextForm";
import {Selector} from "../../components/form/Selector";
import {Button} from "../../components/button";
import {
    ARTICLE,
    GRAPHIC,
    MAP,
    MONOGRAPH,
    PAGE,
    PERIODICAL,
    PERIODICALITEM,
    PERIODICALVOLUME,
    PRIVATE_ACCESS,
    PUBLIC_ACCESS
} from "./constants";
import {useDispatch} from "react-redux";
import {useState} from "react";
import {sendSolrQuery} from "./saga";

export const SolrQueryForm = () => {

    const models = [
        {
            value: MONOGRAPH,
            text: "monograph"
        },
        {
            value: PERIODICAL,
            text: "periodical"
        },
        {
            value: PERIODICALVOLUME,
            text: "periodicalvolume"
        },
        {
            value: PERIODICALITEM,
            text: "periodicalitem"
        },
        {
            value: ARTICLE,
            text: "article"
        },
        {
            value: MAP,
            text: "map"
        },
        {
            value: PAGE,
            text: "page"
        },
        {
            value: GRAPHIC,
            text: "graphic"
        }
    ];
    const accesses = [
        {
            value: PUBLIC_ACCESS,
            text: "public"
        },
        {
            value: PRIVATE_ACCESS,
            text: "private"
        }
    ];

    const dispatch = useDispatch();
    const [from, setFrom] = useState("");
    const [to, setTo] = useState("");
    const [model, setModel] = useState(MONOGRAPH);
    const [access, setAccess] = useState(PUBLIC_ACCESS);

    const handleSend = () => {
        if (from && to) {
            dispatch(sendSolrQuery({
                "year_from": from,
                "year_to": to,
                "model": model,
                "accessibility": access,
            }));
        }
    }

    return <Panel>
        <TextForm
            label="Z roku"
            size="7"
            placeholder="1700"
            onChange={setFrom}
        />
        <TextForm
            label="Do roku"
            size="7"
            placeholder="2021"
            onChange={setTo}
        />
        <Selector
            label="Model"
            options={models}
            onChange={setModel}
        />
        <Selector
            label="Dostupnost"
            options={accesses}
            onChange={setAccess}
        />
        <Button
            label="Zapsat uuid z odpovědi"
            onClick={handleSend}
        />
    </Panel>
};
