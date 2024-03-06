FROM node:18-alpine


RUN useradd -r -u 10014 -g appuser appuser
USER appuser

WORKDIR /app


ENV HOST="0.0.0.0"
ENV DISABLE_DEV_SERVER_HOST_CHECK=true
ENV HTTPS=false

COPY . .



RUN npm install
EXPOSE 3000
CMD ["npm", "start"]