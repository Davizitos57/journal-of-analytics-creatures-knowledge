# JACK — frontend Angular

Base Angular 22 com componentes standalone, roteamento e TypeScript em modo estrito.

O código React publicado continha somente a página inicial do template Vite, com um contador. Essa página foi migrada para `src/app/home`, preservando o contador e os estilos responsivos, com os links e a identificação atualizados para o JACK e o Angular. As telas de agentes, criaturas, rituais e locais ainda precisam ser implementadas.

## Executar localmente

Use Node.js 24.15 ou superior dentro da versão 24. O Docker usa `node:24-alpine`.

Na pasta `frontend/`:

```bash
npm ci
npm start
```

Acesse `http://localhost:5173`. `npm run dev` executa o mesmo servidor para manter o comando usado no Docker. Para gerar a versão de produção:

```bash
npm run build
```

A saída fica em `dist/jack/browser/`. O Dockerfile atual executa o servidor de desenvolvimento; a publicação da saída de produção precisa de um servidor estático com fallback para `index.html` e proxy de API.

## Acesso ao backend

`HttpClient` está configurado em `src/app/app.config.ts`. As chamadas devem usar o prefixo `/api`, por exemplo `/api/rituais` e `/api/locais`.

O arquivo `proxy.conf.mjs` remove esse prefixo antes de encaminhar a chamada, porque os controllers Spring Boot usam `/rituais` e `/locais` diretamente. O navegador acessa a mesma origem do frontend.

| Execução       | Destino do proxy        |
| -------------- | ----------------------- |
| `npm start`    | `http://localhost:8080` |
| Docker Compose | `http://backend:8080`   |

Para executar localmente com outra porta do backend:

```bash
API_PROXY_TARGET=http://localhost:8081 npm start
```

`API_PROXY_TARGET` é lida pelo processo Node do servidor de desenvolvimento. Reinicie o servidor ao mudar seu valor ou o arquivo de proxy. A antiga variável `VITE_API_URL` não é mais usada.

## Organização

| Arquivo ou pasta        | Responsabilidade                                            |
| ----------------------- | ----------------------------------------------------------- |
| `src/main.ts`           | Inicialização do Angular                                    |
| `src/app/app.config.ts` | Providers e configuração da aplicação                       |
| `src/app/app.routes.ts` | Rotas, com carregamento da página inicial sob demanda       |
| `src/app/home/`         | Componente, template e estilos da página inicial            |
| `src/styles.css`        | Estilos globais                                             |
| `public/`               | Imagens e ícones estáticos                                  |
| `angular.json`          | Compilação e servidor de desenvolvimento                    |
| `proxy.conf.mjs`        | Encaminhamento das chamadas à API durante o desenvolvimento |

O estado do contador usa `signal()` no lugar de `useState`. O template HTML usa `(click)` para chamar o método de incremento. O projeto mantém a porta `5173` para os comandos locais e para o container.

## Atualizar um ambiente React existente

Após baixar esta versão, execute `npm ci` para substituir as dependências locais. Com Docker, execute na raiz do repositório:

```bash
docker compose up --build --renew-anon-volumes -d frontend
```

O volume anônimo de `node_modules` precisa ser recriado para não reutilizar as dependências do React. O volume nomeado do banco é preservado.

## Formatação

```bash
npm run format:check
npm run format
```
