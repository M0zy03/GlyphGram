# Publishing on GitHub

## 1. Create the repository

Create an empty public repository named `GlyphGram` on GitHub. Do not add a
README, license, or `.gitignore there — they are already included here.

From this folder run:

```bash
git init
git add .
git commit -m "Initial public release"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/GlyphGram.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username.

## 2. Create release 1.0.1

On GitHub open **Releases → Draft a new release**:

- tag: `v1.0.1`;
- title: `GlyphGram 1.0.1`;
- description: copy `RELEASE_NOTES_1.0.1.md`;
- attach both files from `release-assets/`.

The release files are excluded from normal Git commits by `.gitignore`, so
they must be uploaded to the Release manually.

## 3. Suggested repository settings

- Description: `Glyph lighting for Telegram round-video messages on Nothing Phone (2)`
- Website: `https://t.me/M0zy1`
- Topics: `nothing-phone`, `glyph-interface`, `telegram`, `ayugram`, `exteragram`, `android`
- Enable Issues for bug reports.
