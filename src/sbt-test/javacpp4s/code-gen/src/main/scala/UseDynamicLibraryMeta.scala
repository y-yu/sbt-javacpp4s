import javacpp4s.DynamicLibraryMeta

object UseDynamicLibraryMeta {
  def get: (DynamicLibraryMeta, String) = {
    (
      DynamicLibraryMeta.currentLibraryMeta,
      DynamicLibraryMeta.libraryName
    )
  }
}
