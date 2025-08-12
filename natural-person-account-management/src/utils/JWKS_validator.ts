import type { Context, Next } from 'hono';
import { jwtVerify, createRemoteJWKSet } from 'jose'

var JWKS = createRemoteJWKSet(new URL(process.env.JWKS_URL??""));

export async function verifyToken(jwtToken: string) {
  const { payload, protectedHeader } = await jwtVerify(jwtToken, JWKS, {
    issuer: process.env.JWK_ISSUER,      // same as ’iss’ in JWK
    subject: process.env.JWK_SUBJECT,
    //audience: '<YOUR_AUDIENCE>'// if ’aud’ is set in JWK
    typ: "JWT"
  })
  return payload;
}

export async function jwtValidatorMiddleware(c: Context, next: Next){
    let authHeader = c.req.header("Authorization")?.split(" ")??["",""];
    if(authHeader[0] !== "Bearer"){
        c.status(401);
        return c.text("Unable to parse Authorization Header");
    }
    try {
        let payload = await verifyToken(authHeader[1]);
        c.set("jwtPayload",payload);
        next();
    } catch (e: any){
        c.status(401);
        console.log(e);
        return c.json({
            message:e.code,
            error: e.cause
        });
    }
}