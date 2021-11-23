import React, {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {Box, Grid, TextField} from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";

import {HorizontallyCenteredBox} from "../../components/layout/HorizontallyCenteredBox";
import {AddButton, ClearButton} from "../../components/button";
import {updateVirtualCollection} from "./saga";
import {getIsLoadingError, getVcs} from "./slice";
import {Error} from "../../components/info/Error";

export const EditForm = () => {
    const dispatch = useDispatch();
    const [uuid, setUuid] = useState(null);
    const [nameCz, setNameCz] = useState('');
    const [nameEn, setNameEn] = useState('');
    const [textCz, setTextCz] = useState('');
    const [textEn, setTextEn] = useState('');
    const vcs = useSelector(state => getVcs(state));

    const isLoadingError = useSelector(state => getIsLoadingError(state));

    const anyContent = nameCz || nameEn || textCz || textEn;
    const ready = uuid && nameCz && nameEn && textCz && textEn;

    const updateVc = () => {
        dispatch(updateVirtualCollection({uuid, nameCz, nameEn, textCz, textEn}));
    }

    const loadVcUuid = (event, values) => {
        if (values) {
            setNameCz(values.nameCz);
            setNameEn(values.nameEn);
            setTextCz(values.descriptionCz);
            setTextEn(values.descriptionEn);
        }
        setUuid(values ? values.uuid : '')
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

    const content = (
        <Box>
            <Box style={styles.wrapperStyle}>
                <HorizontallyCenteredBox width={'50%'}>
                    <Autocomplete
                        options={vcs}
                        getOptionLabel={(option) => option.nameCz}
                        onChange={loadVcUuid}
                        renderInput={(params) =>
                            <TextField
                                {...params}
                                label="Název virtuální sbirky"
                                variant="outlined"
                                size="small"
                            />
                        }
                    />
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
                                <ClearButton onClick={handleClear}>Vyčistit</ClearButton>
                            </Grid>
                        )
                    }
                    {
                        ready && (
                            <Grid item xs={6} align="center">
                                <AddButton onClick={updateVc}>Upravit</AddButton>
                            </Grid>
                        )
                    }
                </Grid>
            </Box>
        </Box>
    );

    const error = (
        <HorizontallyCenteredBox>
             <Box mt={"20%"}>
                 <Error label={'Chyba při načtení virtuálních sbírek!'} />
             </Box>
        </HorizontallyCenteredBox>
    );

    return (
      <Box height={"100%"}>
          {
              isLoadingError ? error : content
          }
      </Box>
    );
};
