import superagent from "superagent";
import superagentAbsolute from "superagent-absolute";

const agent = superagent.agent();

/* TODO make backend uri configurable */
export const baseUrl = "http://localhost:8081";
export const request = superagentAbsolute(agent)(baseUrl);
