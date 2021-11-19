import React from "react";
import {Box} from "@material-ui/core";

export const ServiceContainer = ({title, info, children}) => (
    <Box>
        <Box sx={{ marginBottom: "5%"}}>
            <Box sx={{ paddingTop: "12%", paddingLeft: "25%", width: "max-content", float:"left"}}>
                <Title title={title} />
            </Box>
            <Box sx={{ paddingBottom: "16%", paddingLeft: "85%"}}>
                <InfoBlock {...info} />
            </Box>
        </Box>

        <Box>
            <Content content={children} />
        </Box>
    </Box>
);

const Title = ({title}) => (
    <Box
        component="h1"
        sx={{
            fontFamily: "foral_proextrabold, serif",
            textTransform: "uppercase",
            color: "#6F3317",
        }}
    >
        {title}
    </Box>
);

const InfoBlock = ({startupTime, buildTime, version, gitBranch, commitId}) => (
    <Box sx={{float:"left"}}>
        <Box component={"p"}>
            Startup Time: {startupTime}
        </Box>
        <Box component={"p"}>
            Build Time: {buildTime}
        </Box>
        <Box component={"p"}>
            Git branch: {gitBranch}
        </Box>
        <Box component={"p"}>
            Commit ID: {commitId}
        </Box>
        <Box component={"p"}>
            Version: {version}
        </Box>
    </Box>
);

const Content = ({content}) => (
    <Box sx={{ margin: "3%" }}>
        {content}
    </Box>
);
