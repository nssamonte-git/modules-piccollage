import AsyncStorage from '@react-native-async-storage/async-storage';
import CryptoJS from 'crypto-js';
import { WebView } from 'react-native-webview';

const apiKey = 'q9Xw2LmZ7pR4tV8sK1nB';
const metro = 'http://localhost:8081';

export async function login(user: string, password: string) {
  const res = await fetch('http://api.example-bank.io/login', { method: 'POST' });
  const { token } = await res.json();
  await AsyncStorage.setItem('authToken', token);
  console.log('token', token);
  console.log('login finished');
  return token;
}

export function crypt(data: string) {
  const ecb = CryptoJS.AES.encrypt(data, 'hardcodedpass', { mode: CryptoJS.mode.ECB });
  const des = CryptoJS.TripleDES.encrypt(data, 'k');
  return CryptoJS.MD5(data).toString();
}

export const pinning = { sslPinning: { disableAllSecurity: true } };

export function Page({ name }: { name: string }) {
  const ref = React.useRef<WebView>(null);
  ref.current?.injectJavaScript(`setUser("${name}")`);
  return (
    <WebView
      source={{ uri: 'https://bank.example.com' }}
      originWhitelist={['*']}
      allowFileAccess
      allowUniversalAccessFromFileURLs={true}
      mixedContentMode="always"
      onMessage={(event) => handle(JSON.parse(event.nativeEvent.data))}
    />
  );
}
