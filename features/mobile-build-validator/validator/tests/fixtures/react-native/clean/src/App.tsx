import * as Keychain from 'react-native-keychain';
import { Linking } from 'react-native';
import { WebView } from 'react-native-webview';

const ALLOWED_HOSTS = ['good.example.com'];

export async function login() {
  const res = await fetch('https://api.example.com/login', { method: 'POST' });
  const { token } = await res.json();
  await Keychain.setGenericPassword('session', token, { accessible: Keychain.ACCESSIBLE.WHEN_UNLOCKED_THIS_DEVICE_ONLY });
  console.log('login finished');
}

export async function boot(navigate: (path: string) => void) {
  const raw = await Linking.getInitialURL();
  if (!raw) return;
  const url = new URL(raw);
  if (ALLOWED_HOSTS.includes(url.host)) navigate(url.pathname);
}

export function Page() {
  return (
    <WebView
      source={{ uri: 'https://good.example.com' }}
      originWhitelist={['https://good.example.com']}
      onMessage={(event) => {
        if (new URL(event.nativeEvent.url).host !== 'good.example.com') return;
      }}
    />
  );
}
