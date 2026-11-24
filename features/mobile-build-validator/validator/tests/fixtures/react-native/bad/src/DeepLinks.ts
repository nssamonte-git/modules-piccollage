import { Linking } from 'react-native';

export async function boot(navigate: (path: string) => void) {
  const url = await Linking.getInitialURL();
  if (url) navigate(url.replace('badapp://', ''));
}
