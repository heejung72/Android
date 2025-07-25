package com.example.flo_mainpage.Album

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flo_mainpage.Like

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    // 좋아요 관련 메서드들
    @Insert
    fun likeAlbum(like: Like)

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikeAlbum(userId: Int, albumId: Int)

    @Query("SELECT userId FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikedAlbum(userId: Int, albumId: Int): Int?

    @Query("SELECT * FROM AlbumTable INNER JOIN LikeTable ON AlbumTable.id = LikeTable.albumId WHERE LikeTable.userId = :userId")
    fun getLikedAlbums(userId: Int): List<Album>

    // 삭제 메서드 추가
    @Query("DELETE FROM AlbumTable WHERE id = :albumId")
    fun deleteAlbumById(albumId: Int)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album?

    @Query("UPDATE AlbumTable SET isLike= :isLike WHERE id = :id")
    fun updateIsLikeById(isLike: Boolean,id: Int)

    @Query("SELECT * FROM AlbumTable WHERE isLike= :isLike")
    fun getLikedAlbums(isLike: Boolean): List<Album>

    @Update
    fun updateAlbum(album: Album)





}