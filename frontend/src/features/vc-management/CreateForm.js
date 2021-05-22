import {useState} from "react";
import {useDispatch} from "react-redux";
import {Box, Grid, TextField} from "@material-ui/core";

import {HorizontallyCenteredBox} from "../../components/temporary/HorizontallyCenteredBox";
import {AddButton, ClearButton} from "../../components/button";
import {createVirtualCollection} from "./saga";

export const CreateForm = () => {
    const dispatch = useDispatch();
    const [nameCz, setNameCz] = useState('');
    const [nameEn, setNameEn] = useState('');
    const [textCz, setTextCz] = useState('');
    const [textEn, setTextEn] = useState('');

    const anyContent = nameCz || nameEn || textCz || textEn;
    const ready = nameCz && nameEn && textCz && textEn;

    const createVc = () => {
        dispatch(createVirtualCollection({nameCz, nameEn, textCz, textEn}));
    }

    const handleClear = () => {
        setNameCz('');
        setNameEn('');
        setTextCz('');
        setTextEn('');
    }

    const styles = {
        stringInputStyle: {
            width: '100%',
        },
        textInputStyle: {
            width: '100%',
        },
        wrapperStyle: {
            padding: '30px 15px 0 30px',
        },
        containerStyle: {
            width: '100%',
        }
    }

    const containerProps = {
        spacing: 2,
        alignContent: "center",
        justifyContent: "center"
    }

    return <Box>
        <Box style={styles.wrapperStyle}>
            <HorizontallyCenteredBox width={'70%'}>
                <Box style={{padding: '9px'}}>
                    Zadejte názvy a texty virtuální sbírky v češtině a angličtině, Kramerius doplní UUID sbírky samostatně.
                </Box>
            </HorizontallyCenteredBox>
        </Box>

        <Box style={styles.wrapperStyle}>
            <Grid container style={styles.containerStyle} {...containerProps}>
                <Grid item xs={6}>
                    <TextField
                        label="Název virtuální sbírky v češtině"
                        variant="outlined"
                        value={nameCz}
                        style={styles.stringInputStyle}
                        onChange={e => setNameCz(e.target.value)}
                    />
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        label="Název virtuální sbírky v angličtině"
                        variant="outlined"
                        value={nameEn}
                        style={styles.stringInputStyle}
                        onChange={e => setNameEn(e.target.value)}
                    />
                </Grid>
            </Grid>
        </Box>

        <Box style={styles.wrapperStyle}>
            <Grid container style={styles.containerStyle} {...containerProps}>
                <Grid item xs={6}>
                    <TextField
                        label="Popis virtuální sbírky v češtině"
                        multiline
                        rows={10}
                        value={textCz}
                        style={styles.textInputStyle}
                        variant="outlined"
                        onChange={e => setTextCz(e.target.value)}
                    />
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        label="Popis virtuální sbírky v angličtině"
                        multiline
                        rows={10}
                        value={textEn}
                        style={styles.textInputStyle}
                        variant="outlined"
                        onChange={e => setTextEn(e.target.value)}
                    />
                </Grid>
            </Grid>
        </Box>

        <Box style={styles.wrapperStyle}>
            <Grid container style={styles.containerStyle}>
                {
                    anyContent && (
                        <Grid item xs={6} align="center">
                            <ClearButton onClick={handleClear}>Clear</ClearButton>
                        </Grid>
                    )
                }
                {
                    ready && (
                        <Grid item xs={6} align="center">
                            <AddButton onClick={createVc}>Vytvořit</AddButton>
                        </Grid>
                    )
                }
            </Grid>
        </Box>
    </Box>;
};
