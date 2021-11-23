import {Box, Grid, TextField} from "@material-ui/core";
import {AddButton, ClearButton, UploadButton} from "../../components/button";
import CheckCircleOutlineIcon from "@material-ui/icons/CheckCircleOutline";

export const styles = {
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
};

const containerProps = {
    spacing: 2,
    alignContent: "center",
    justifyContent: "center"
};

export const VCNames = ({nameCz, nameEn, setNameCz, setNameEn}) => {
    return (
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
    );
};

export const VCDescriptions = ({textCz, textEn, setTextCz, setTextEn}) => {
    return (
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
    );
};

export const Buttons = ({anyContent, ready, handleClear, createVc, fullImg, setFullImg, thumbImg, setThumbImg}) => {
    return (
        <Box style={styles.wrapperStyle}>
            <Grid container style={styles.containerStyle}>
                <Grid item xs={3} align="center">
                    <UploadImgButton img={fullImg} setFunc={setFullImg} label={"Vybrat FULL obrázek"} />
                </Grid>
                <Grid item xs={3} align="center">
                    <UploadImgButton img={thumbImg} setFunc={setThumbImg} label={"Vybrat THUMB obrázek"} />
                </Grid>
                {
                    anyContent && (
                        <Grid item xs={3} align="center">
                            <ClearButton onClick={handleClear}>Vyčistit</ClearButton>
                        </Grid>
                    )
                }
                {
                    ready && (
                        <Grid item xs={3} align="center">
                            <AddButton onClick={createVc}>Vytvořit</AddButton>
                        </Grid>
                    )
                }
            </Grid>
        </Box>
    );
};

const UploadImgButton = ({label, img, setFunc}) => {
    const uploadImg = e => {
        const file = e.target.files[0];
        setFunc(file);
    };

    return (
        <Grid container justifyContent={"center"} alignItems={"center"}>
            <Grid item>
                <UploadButton onChange={uploadImg}>
                    {label}
                    <input type="file" hidden />
                </UploadButton>
            </Grid>
            {
                img && (
                    <Grid item>
                        <CheckCircleOutlineIcon />
                    </Grid>
                )
            }
        </Grid>
    );
};
