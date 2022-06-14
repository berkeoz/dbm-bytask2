CREATE TABLE [dbo].[Genre]
(
    [GenreId] INT NOT NULL,
    [Name] NVARCHAR(120),
    CONSTRAINT [PK_Genre] PRIMARY KEY CLUSTERED ([GenreId])
);