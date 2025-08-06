import { Hono } from "hono";
import { describeRoute } from "hono-openapi";
import { resolver, validator } from "hono-openapi/zod";
import { NaturalPersonIdValidator, type NaturalPersonId } from "../schemas/NaturalPersonId.js";
import z from "zod";
import { APP_NAME } from "../utils/config.js";
import { jwtValidatorMiddleware } from "../utils/JWKS_validator.js";
import BusinessException from "../utils/BusinessException.js";
import { AccountValidator } from "../schemas/Account.js";
import AccountController from "../controllers/AccountController.js";
import { GetAccountValidator, type GetAccount } from "../schemas/GetAccount.js";

var router = new Hono();

router.post("/retreive-customer-account",
  describeRoute({
    description: "Retreive Natural Person data",
    tags: ["Natural Person"],
    responses: {
      200: {
        description: "Natural Person data",
        content: {
          'application/json': {
            schema: resolver(AccountValidator)
          }
        }
      }
    }
  }),
  validator("header", z.object({"message-id": z.string(), Authorization:z.string()}), (err, c)=>{
    if(!err.success){
      c.status(400);
      return c.json({error: "unable to parse headers", message: JSON.parse(err.error.message)});
    }
  }),
  validator('json', GetAccountValidator, (err, c)=>{
    if(!err.success){
      c.status(400);
      return c.json({error: "unable to parse body", message: JSON.parse(err.error.message)});
    }
  }),
  jwtValidatorMiddleware,
  async c=>{
    var {id, type}:GetAccount = await c.req.json();
    try {
      let person = await AccountController.getAccount(id, type);
      c.status(200);
      return c.json({
        data: person,
        meta: {
          _messageId: c.req.header("message-id"),
          _applicationId: APP_NAME,
          _requestDateTime: (new Date(Date.now())).toISOString()
        }
      });
    } catch(e: any){
      if(e instanceof BusinessException){
        c.status(e.statusCode);
        return c.json({
          error: [{
            message: e.message,
            code: e.code
          }],
          meta: {
            _messageId: c.req.header("message-id"),
            _applicationId: APP_NAME,
            _requestDateTime: (new Date(Date.now())).toISOString()
          }
        })
      }
      c.status(400);
      return c.json({
        error: "unable to handle request",
        message: e.message
      });
    }
  }
);


export default router;