import React, { useState, useEffect } from "react";
import logo from "./logo.svg";
import "./App.css";
import { BasicUserInfo, useAuthContext } from "@asgardeo/auth-react";
import {
  StyleSheet,
  Button,
  View,
  SafeAreaView,
  Text,
  Alert,
} from "react-native";

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
  const [token, setToken] = useState("");

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
    const token = await getAccessToken();
    setToken(token);
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
    return (
      <div className="App">
        <header className="App-header">
          <h1>
            <p>Unlock health: Share your unused meds.</p>
          </h1>
          <img src={logo} className="App-logo" alt="logo" />
          <View>
            <Button title="LOGIN" onPress={handleSignIn} />
          </View>
        </header>
      </div>
    );
  }
  return (
    <div className="App">
      <h1>Logged in: {user?.username}</h1>
      <h1>Token: {token}</h1>
    </div>
  );
}

export default App;
