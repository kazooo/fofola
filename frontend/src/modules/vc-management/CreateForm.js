import {useState} from "react";
import {useDispatch} from "react-redux";
import {Box} from "@material-ui/core";

import {HorizontallyCenteredBox} from "../../components/layout/HorizontallyCenteredBox";
import {Buttons, styles, VCDescriptions, VCNames} from "./components";
import {createVirtualCollection} from "./saga";

export const CreateForm = () => {
    const dispatch = useDispatch();
    const [nameCz, setNameCz] = useState('');
    const [nameEn, setNameEn] = useState('');
    const [textCz, setTextCz] = useState('');
    const [textEn, setTextEn] = useState('');
    const [fullImg, setFullImg] = useState(null);
    const [thumbImg, setThumbImg] = useState(null);

    const createVc = () => {
        dispatch(createVirtualCollection({nameCz, nameEn, textCz, textEn, fullImg, thumbImg}));
    }

    const handleClear = () => {
        setNameCz('');
        setNameEn('');
        setTextCz('');
        setTextEn('');
        setFullImg(null);
        setThumbImg(null);
    }

    const buttonFuncs = {
        createVc,
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
            textCz={textCz}
            textEn={textEn}
            setTextCz={setTextCz}
            setTextEn={setTextEn}
        />

        <Buttons
            anyContent={nameCz || nameEn || textCz || textEn || fullImg || thumbImg}
            ready={nameCz && nameEn && textCz && textEn}
            fullImg={fullImg}
            thumbImg={thumbImg}
            {...buttonFuncs}
        />
    </Box>;
};
