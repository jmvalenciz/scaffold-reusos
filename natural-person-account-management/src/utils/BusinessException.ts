import type { StatusCode } from "hono/utils/http-status";

export default class BusinessException extends Error{
    statusCode: StatusCode;
    code: string;
    constructor(message: string, statusCode: StatusCode, code: string){
        super(message);
        this.statusCode = statusCode;
        this.code = code;
    }
}