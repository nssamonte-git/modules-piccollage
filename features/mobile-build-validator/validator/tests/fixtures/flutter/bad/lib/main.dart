import 'dart:io';
import 'package:crypto/crypto.dart';
import 'package:encrypt/encrypt.dart';

const apiKey = 'q9Xw2LmZ7pR4tV8sK1nB';
const endpoint = 'http://api.example-bank.io/v1';
const local = 'http://localhost:8080';

HttpClient insecureClient() {
  final client = HttpClient();
  client.badCertificateCallback = (cert, host, port) => true;
  return client;
}

Future<void> store(SharedPreferences prefs, String token) async {
  await prefs.setString('auth_token', token);
  await secureStorage.write(key: 'auth_token', value: token);
  debugPrint('token: $token');
  print('loaded ${items.length} items');
}

String crypt(String text) {
  final key = Key.fromUtf8('0123456789abcdef0123456789abcdef');
  final iv = IV.fromLength(16);
  final aes = Encrypter(AES(key, mode: AESMode.ecb));
  final digest = md5.convert(text.codeUnits);
  return aes.encrypt(text, iv: iv).base64;
}

void web(WebViewController controller, String name) {
  controller.addJavaScriptChannel('Native', onMessageReceived: (m) {});
  controller.runJavaScript('setUser("$name")');
}

final settings = InAppWebViewSettings(
  allowFileAccessFromFileURLs: true,
  mixedContentMode: MixedContentMode.MIXED_CONTENT_ALWAYS_ALLOW,
);

Future<ServerTrustAuthResponse?> onTrust(controller, challenge) async {
  return ServerTrustAuthResponse(action: ServerTrustAuthResponseAction.PROCEED);
}
