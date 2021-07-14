import superagent from "superagent";
import superagentAbsolute from "superagent-absolute";

const agent = superagent.agent();

/* TODO make backend uri configurable */
export const request = superagentAbsolute(agent)("http://localhost:8081");
