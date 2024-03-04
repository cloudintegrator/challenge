import { BasicUserInfo } from "@asgardeo/auth-react";
import React, { FunctionComponent, ReactElement } from "react";
import { JsonViewer } from '@textea/json-viewer'

/**
 * Decoded ID Token Response component Prop types interface.
 */
interface AuthenticationResponsePropsInterface {
    /**
     * Derived Authenticated Response.
     */
    derivedResponse?: any;
}

export interface DerivedAuthenticationResponseInterface {
    /**
     * Response from the `getBasicUserInfo()` function from the SDK context.
     */
    authenticateResponse: BasicUserInfo;
    /**
     * ID token split by `.`.
     */
    idToken: string[];
    /**
     * Decoded Header of the ID Token.
     */
    decodedIdTokenHeader: Record<string, unknown>;
    /**
     * Decoded Payload of the ID Token.
     */
    decodedIDTokenPayload: Record<string, unknown>;
}

/**
 * Displays the derived Authentication Response from the SDK.
 *
 * @param {AuthenticationResponsePropsInterface} props - Props injected to the component.
 *
 * @return {React.ReactElement}
 */
export const AuthenticationResponse: FunctionComponent<AuthenticationResponsePropsInterface> = (
    props: AuthenticationResponsePropsInterface
): ReactElement => {

    const {
        derivedResponse
    } = props;

    return (
        <>
            <h2>Authentication Response</h2>
            <h2 className="mb-0 mt-4">ID token</h2>
            <div className="row">
                { derivedResponse?.idToken && (
                    <div className="column">
                        <h5>
                            <b>Encoded</b>
                        </h5>
                        <div className="code">
                            <code>
                                <span className="id-token-0">{ derivedResponse?.idToken[0] }</span>.
                                <span className="id-token-1">{ derivedResponse?.idToken[1] }</span>.
                                <span className="id-token-2">{ derivedResponse?.idToken[2] }</span>
                            </code>
                        </div>
                    </div>
                ) }
            </div>
        </>
    );
};
