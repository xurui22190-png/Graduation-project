import { defineConfig, globalIgnores } from "eslint/config";
import globals from "globals";
import js from "@eslint/js";
import pluginVue from "eslint-plugin-vue";
import pluginOxlint from "eslint-plugin-oxlint";

export default defineConfig([
  {
    name: "app/files-to-lint",
    files: ["**/*.{vue,js,mjs,jsx}"],
  },

  globalIgnores(["**/dist/**", "**/dist-ssr/**", "**/coverage/**"]),

  {
    languageOptions: {
      globals: {
        ...globals.browser,
      },
    },
  },

  js.configs.recommended,
  ...pluginVue.configs["flat/essential"],

  // ⭐ 在这里关闭 Vue 命名规范
  {
    rules: {
      "vue/multi-word-component-names": "off",
    },
  },
  {
    rules: {
      "no-unused-vars": "off",
    },
  },
  {
    rules: {
      "no-unused-expressions": "off",
    },
  },
  ...pluginOxlint.buildFromOxlintConfigFile(".oxlintrc.json"),
]);
