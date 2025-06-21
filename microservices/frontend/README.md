# Frontend React - Sistema de Vendas

Este é o frontend React para o sistema de vendas com microserviços. O frontend fornece uma interface moderna e responsiva para interagir com todos os microserviços.

## 🚀 Funcionalidades

### 📊 Dashboard
- Visão geral do sistema
- Status dos microserviços em tempo real
- Ações rápidas para navegação
- Cards informativos sobre cada módulo

### 📦 Gerenciamento de Produtos
- Listagem de produtos com informações detalhadas
- Criação de novos produtos
- Edição de produtos existentes
- Controle de estoque (reposição)
- Status visual do estoque (verde, amarelo, vermelho)

### 🛒 Gerenciamento de Pedidos
- Criação de novos pedidos
- Seleção de produtos e quantidades
- Confirmação de pedidos
- Visualização detalhada de pedidos
- Histórico de pedidos por período

### 🧮 Calculadora de Impostos
- Cálculo de impostos por estado (RS, SP, PE)
- Seleção de produtos e quantidades
- Visualização detalhada dos cálculos
- Informações sobre as regras de impostos por estado
- Resultados em tempo real

### 📈 Relatórios
- Geração de relatórios mensais
- Visualização de vendas por estado
- Produtos mais vendidos
- Vendas diárias
- Histórico de relatórios

### 🔍 Monitoramento de Saúde
- Status em tempo real de todos os microserviços
- Informações sobre a arquitetura
- Links diretos para dashboards dos serviços
- Solução de problemas

## 🛠️ Tecnologias Utilizadas

- **React 18** - Biblioteca principal
- **React Router DOM** - Roteamento
- **React Hook Form** - Gerenciamento de formulários
- **Axios** - Cliente HTTP
- **React Hot Toast** - Notificações
- **Lucide React** - Ícones
- **Tailwind CSS** - Estilização

## 📋 Pré-requisitos

- Node.js 16+ 
- npm ou yarn
- Microserviços rodando (ver README principal)

## 🚀 Instalação e Execução

1. **Instalar dependências:**
```bash
cd frontend
npm install
```

2. **Executar em modo de desenvolvimento:**
```bash
npm start
```

3. **Acessar a aplicação:**
```
http://localhost:3000
```

## 🔧 Configuração

### Variáveis de Ambiente
O frontend está configurado para se conectar aos microserviços através do API Gateway na porta 8080. Se necessário, você pode alterar a URL base no arquivo `src/services/api.js`:

```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

### Proxy
O projeto está configurado com proxy para o API Gateway no `package.json`:

```json
{
  "proxy": "http://localhost:8080"
}
```

## 📁 Estrutura do Projeto

```
src/
├── components/
│   └── Layout.js          # Layout principal com navegação
├── pages/
│   ├── Dashboard.js       # Página inicial
│   ├── Products.js        # Gerenciamento de produtos
│   ├── Orders.js          # Gerenciamento de pedidos
│   ├── TaxCalculator.js   # Calculadora de impostos
│   ├── Reports.js         # Relatórios
│   └── Health.js          # Monitoramento de saúde
├── services/
│   └── api.js             # Cliente API para microserviços
├── App.js                 # Componente principal
├── index.js               # Ponto de entrada
└── index.css              # Estilos globais
```

## 🔌 Integração com Microserviços

### API Gateway (Porta 8080)
- Todas as requisições passam pelo API Gateway
- Roteamento automático para os microserviços
- Centralização de autenticação e autorização

### Sales Service (Porta 8081)
- Gerenciamento de produtos
- Criação e confirmação de pedidos
- Consulta de pedidos por período

### Tax Service (Porta 8082)
- Cálculo de impostos por estado
- Regras específicas para RS, SP e PE

### Sales Registry Service (Porta 8083)
- Geração de relatórios mensais
- Registro de vendas confirmadas

## 🎨 Design System

### Cores
- **Primary**: Azul (#3B82F6)
- **Success**: Verde (#10B981)
- **Warning**: Amarelo (#F59E0B)
- **Error**: Vermelho (#EF4444)

### Componentes
- Cards com sombras suaves
- Botões com estados hover
- Formulários com validação visual
- Tabelas responsivas
- Modais e overlays

## 📱 Responsividade

O frontend é totalmente responsivo e funciona em:
- Desktop (1024px+)
- Tablet (768px - 1023px)
- Mobile (320px - 767px)

## 🔍 Funcionalidades Avançadas

### Validação de Formulários
- Validação em tempo real
- Mensagens de erro contextuais
- Prevenção de envio com dados inválidos

### Estados de Carregamento
- Spinners durante requisições
- Estados de loading por componente
- Feedback visual para ações

### Notificações
- Toast notifications para sucesso/erro
- Posicionamento no canto superior direito
- Auto-dismiss configurável

### Navegação
- Sidebar responsiva
- Indicador de página ativa
- Navegação por breadcrumbs

## 🚀 Build para Produção

```bash
npm run build
```

O build será gerado na pasta `build/` e pode ser servido por qualquer servidor web estático.

## 🔧 Scripts Disponíveis

- `npm start` - Executa em modo de desenvolvimento
- `npm run build` - Gera build de produção
- `npm test` - Executa testes
- `npm run eject` - Ejecta configurações do Create React App

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 🆘 Suporte

Para suporte, entre em contato com a equipe de desenvolvimento ou abra uma issue no repositório. 