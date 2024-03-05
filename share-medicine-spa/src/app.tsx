import React, { useState, useEffect } from "react";
import logo from "./logo.svg";
import "./App.css";
import { BasicUserInfo, useAuthContext } from "@asgardeo/auth-react";

function App() {
  const {
    signIn,
    signOut,
    getAccessToken,
    isAuthenticated,
    getBasicUserInfo,
    state,
  } = useAuthContext();
  const [isAuthLoading, setIsAuthLoading] = useState(false);
  const [signedIn, setSignedIn] = useState(false);
  const [user, setUser] = useState<BasicUserInfo | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const sleep = (ms: number) => new Promise((r) => setTimeout(r, ms));

  useEffect(() => {
    async function signInCheck() {
      setIsAuthLoading(true);
      await sleep(2000);
      const isSignedIn = await isAuthenticated();
      setSignedIn(isSignedIn);
      setIsAuthLoading(false);
      return isSignedIn;
    }
    signInCheck().then((res) => {
      if (res) {
        getUser();
      } else {
        console.log("User has not signed in");
      }
    });
  }, []);
  async function getUser() {
    setIsLoading(true);
    const userResponse = await getBasicUserInfo();
    setUser(userResponse);
    setIsLoading(false);
  }

  const handleSignIn = async () => {
    signIn()
      .then(() => {
        setSignedIn(true);
      })
      .catch((e) => {
        console.log(e);
      });
  };
  if (isAuthLoading) {
    return <div className="animate-spin h-5 w-5 text-white">.</div>;
  }

  if (!signedIn) {
    return <button onClick={handleSignIn}>Login</button>;
  }
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
      </header>
    </div>
  );
}

export default App;
