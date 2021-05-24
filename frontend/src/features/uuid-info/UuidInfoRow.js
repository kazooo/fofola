import {useDispatch} from "react-redux";
import {MakePrivateButton, MakePublicButton, ReindexButton} from "../../components/button";
import {privateUuids, publicUuids, reindexUuids} from "./saga";

export const UuidInfoRow = ({data}) => {

    const dispatch = useDispatch();

    return <tr>
        <td>{data.uuid}</td>
        <td>{data.model}</td>
        <td>{data.isIndexed}/{data.isStored}</td>
        <td>{data.accessibilityInSolr}/{data.accessibilityInFedora}</td>
        <td>{data.imgUrl}</td>
        <td>{data.rootTitle}</td>
        <td>{data.solrModifiedDate}<br/>{data.fedoraModifiedDate}</td>
        <td>
            <ReindexButton onClick={() => dispatch(reindexUuids([data.uuid]))}/>
            <MakePublicButton onClick={() => dispatch(publicUuids([data.uuid]))}/>
            <MakePrivateButton onClick={() => dispatch(privateUuids([data.uuid]))}/>
        </td>
    </tr>
};
