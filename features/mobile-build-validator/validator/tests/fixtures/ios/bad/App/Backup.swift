import Foundation

func persist(_ data: Data) throws {
    let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
    try data.write(to: dir.appendingPathComponent("cache.bin"))
}
