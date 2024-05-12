package common.compressor;

public interface Compressor {
    /**
     * 压缩字节数组
     * @param bytes 要压缩的字节数组
     * @return 压缩后的字节数组
     */
    byte[] compress(byte[] bytes);

    /**
     * 解压缩字节数组
     * @param bytes 要解压缩的字节数组
     * @return 解压缩后的字节数组
     */
    byte[] decompress(byte[] bytes);
}