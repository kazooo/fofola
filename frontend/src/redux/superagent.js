import superagent from "superagent";
import superagentAbsolute from "superagent-absolute";

const agent = superagent.agent();
export const request = superagentAbsolute(agent)("http://localhost:8081");
