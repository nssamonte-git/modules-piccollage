import 'package:flutter_secure_storage/flutter_secure_storage.dart';

const endpoint = 'https://api.example.com/v1';
const storage = FlutterSecureStorage();

Future<void> store(String token) async {
  await storage.write(key: 'auth_token', value: token);
  debugPrint('token refreshed');
}
