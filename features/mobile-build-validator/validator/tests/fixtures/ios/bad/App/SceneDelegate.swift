import UIKit

final class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    func scene(_ scene: UIScene, openURLContexts URLContexts: Set<UIOpenURLContext>) {
        guard let url = URLContexts.first?.url else { return }
        Router.shared.open(url)
    }
}
