package example.db

import java.time.ZonedDateTime

/**
 * リード用のTODOモデル。
 *
 * @param id 識別子
 * @param text TODOの文字列
 * @param createAt 作成日時
 * @param updateAt 更新日時
 */
case class Todo(id: TodoId, text: String, createAt: ZonedDateTime, updateAt: ZonedDateTime)
