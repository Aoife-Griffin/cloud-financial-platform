### Stage 1: Build React Production Assets
FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

### Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
# Highlight-Start: Looks inside the local build context instead of using ../
COPY default.conf /etc/nginx/conf.d/default.conf
# Highlight-End
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
