import {useState} from "react";
import {useDispatch} from "react-redux";
import {Box} from "@material-ui/core";

import {HorizontallyCenteredBox} from "../../components/layout/HorizontallyCenteredBox";
import {Buttons, styles, VCDescriptions, VCNames} from "./components";
import {createVirtualCollection} from "./saga";
import {AddButton} from "../../components/button";

export const CreateForm = () => {
    const dispatch = useDispatch();
    const [nameCz, setNameCz] = useState('');
    const [nameEn, setNameEn] = useState('');
    const [descriptionCz, setDescriptionCz] = useState('');
    const [descriptionEn, setDescriptionEn] = useState('');
    const [fullImg, setFullImg] = useState(null);
    const [thumbImg, setThumbImg] = useState(null);

    const createVc = () => {
        dispatch(createVirtualCollection({nameCz, nameEn, descriptionCz, descriptionEn, fullImg, thumbImg}));
    }

    const handleClear = () => {
        setNameCz('');
        setNameEn('');
        setDescriptionCz('');
        setDescriptionEn('');
        setFullImg(null);
        setThumbImg(null);
    }

    const buttonFuncs = {
        actionButton: <AddButton onClick={createVc}>Vytvořit</AddButton>,
        handleClear,
        setFullImg,
        setThumbImg,
    }

    return <Box>
        <Box style={styles.wrapperStyle}>
            <HorizontallyCenteredBox width={'70%'}>
                <Box style={{padding: '9px'}}>
                    Zadejte názvy a texty virtuální sbírky v češtině a angličtině, Kramerius doplní UUID sbírky samostatně.
                </Box>
            </HorizontallyCenteredBox>
        </Box>

        <VCNames
            nameCz={nameCz}
            nameEn={nameEn}
            setNameCz={setNameCz}
            setNameEn={setNameEn}
        />

        <VCDescriptions
            textCz={descriptionCz}
            textEn={descriptionEn}
            setTextCz={setDescriptionCz}
            setTextEn={setDescriptionEn}
        />

        <Buttons
            anyContent={nameCz || nameEn || descriptionCz || descriptionEn || fullImg || thumbImg}
            ready={nameCz && nameEn && descriptionCz && descriptionEn}
            fullImg={fullImg}
            thumbImg={thumbImg}
            {...buttonFuncs}
        />
    </Box>;
};
