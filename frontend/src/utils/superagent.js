import superagent from "superagent";
import superagentAbsolute from "superagent-absolute";
import {BASE_URL} from "./environment";

const agent = superagent.agent();

export const request = superagentAbsolute(agent)(BASE_URL);
