import {useDispatch, useSelector} from "react-redux";
import {Box, Grid, TextField} from "@material-ui/core";
import Autocomplete from '@material-ui/lab/Autocomplete';

import {setMode, addUuids, setVcUuid, getMode, getVcs} from "./slice";
import {modes} from "./constants";

import {LoadingComponent} from "../../components/temporary/LoadingComponent";
import {LoadUuidsForm} from "../../components/temporary/LoadUuidsForm";
import {Selector} from "../../components/temporary/Selector";

export const LinkVcForm = () => {

    const dispatch = useDispatch();
    const mode = useSelector(state => getMode(state));
    const vcs = useSelector(state => getVcs(state));

    const loadVcUuid = (event, values) => {
        dispatch(setVcUuid(values ? values.uuid : ''));
    }

    const changeMode = mode => {
        dispatch(setMode(mode));
    }

    const loadUuids = (uuids) => {
        dispatch(addUuids(uuids));
    }

    const form = <Box>
        <LoadUuidsForm addUuids={loadUuids} />
        <Box>
            <Grid container
                  direction="row"
                  alignItems={"center"}
                  justifyContent={"center"}
                  spacing={3}
            >
                <Grid item>
                    <Autocomplete
                        options={vcs}
                        getOptionLabel={(option) => option.nameCz}
                        style={{ width: 300 }}
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
                </Grid>
                <Grid item>
                    <Selector
                        selectLabel={"Režim"}
                        selectOptions={modes}
                        selectedOption={mode}
                        onSelectOptionChange={changeMode}
                    />
                </Grid>
            </Grid>
        </Box>
    </Box>

    return <Box>
        {
            vcs.length > 0
                ? form
                : <LoadingComponent label={'Načítám virtuální sbirky...'} />
        }
    </Box>
};
