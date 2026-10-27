# 学生・教員向け情報システム / School Information System

専門学校のチーム開発で制作した、学生と教員が利用するWebアプリケーションです。
時間割、成績、出欠、試験・行事のお知らせ、教員からのコメントやファイル共有を一つのシステムで管理します。
私はバックエンドの設計・実装とサーバーへのデプロイを担当しました。

**技術:** Java 17 · Spring Boot 3.3 · Spring Security · Spring Data JPA · PostgreSQL · Thymeleaf · Docker

## 主な機能

- **権限に応じた画面の切り替え**（学生／教科担当／担任／管理者）
  - 学生：自分の時間割、成績、出欠、お知らせ、教員のコメントとファイルを閲覧。日ごとのメモ（教員への公開も可）
  - 教科担当：担当授業の成績・出欠の入力、コメント、ファイル添付、行事の登録
  - 担任：クラス全体の時間割と、クラスの成績・出欠の一覧
  - 管理者：ユーザー、学科、クラス、科目、学期の管理、全データの編集
- **時間割の自動生成**：週ごとのテンプレートを学期の期間内で日付つきの授業に展開。
  祝日・休暇、振替日（別の曜日の時間割を適用する日）に対応し、全体／学科／クラス単位で設定可能。
  再実行しても授業は重複しません
- **パスワード再設定**：メールで期限付きリンクを送信
- **多言語対応**：日本語（既定）・英語・ロシア語を画面上で切り替え
- 写真付きのユーザー情報、学籍番号（6桁）の形式・重複チェック、削除時の整合性チェック

## 構成

```
src/main/java/com/school
├── model/      JPAエンティティ（ユーザー、クラス、学科、学期、授業、成績、出欠、祝日、振替日 など）
├── repo/       Spring Data リポジトリ
├── security/   ログインとロール別のアクセス制御
├── service/    アクセス判定、時間割生成、祝日判定、ファイル保存、メール送信
├── web/        コントローラー（学生／教員／管理者／プロフィール／ファイル）
└── config/     初期データ投入、多言語設定
```

## 起動方法

PostgreSQLにデータベース `school` を作成し、以下を実行します。

```bash
mvn spring-boot:run
```

http://localhost:8080 を開くとログイン画面が表示されます。
接続先は環境変数 `DATABASE_URL` / `DATABASE_USERNAME` / `DATABASE_PASSWORD` で変更できます。
Dockerfile と `render.yaml` により、Render（アプリ）＋ Neon（PostgreSQL）へデプロイできます。

初回起動時に学科とデモ用アカウントが作成されます：`admin` / `admin123`（管理者）、`teacher1`・`teacher2` / `teacher123`（教員）。

---

## English

A web application for students and teachers, built as a school team project.
It manages timetables, grades, attendance, exam and event notices, and teacher comments and files.
I was responsible for the backend design and implementation and for deploying it to a server.

- Role-based views for students, subject teachers, homeroom teachers and administrators
- Timetable generation from weekly templates within each term, with holidays and day substitutions
  at school, course or class level; regeneration never creates duplicates
- Password reset by email, file uploads, user photos, Japanese / English / Russian UI

**Stack:** Java 17, Spring Boot 3.3, Spring Security, Spring Data JPA, PostgreSQL, Thymeleaf, Docker.
Run with `mvn spring-boot:run` against a PostgreSQL database named `school`.
