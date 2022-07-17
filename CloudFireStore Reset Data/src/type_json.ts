export type JS_Categories = JS_Category[]

export interface JS_Category {
  title: string
  lien: string
  description: string
  image: JS_Image[]
  subcategories: JS_Subcategory[]
}

export interface JS_Image {
  url: string
  file: string
}

export interface JS_Subcategory {
  title: string
  exercises: JS_Exercise[]
}

export interface JS_Exercise {
  title: string
  lien: string
  category: string
  short_description: string
  image_short: JS_Image[]
  image: JS_Image[]
  description: string
  sections: JS_Section[]
}



export interface JS_Section {
  title: string
  content: string
  video?: string[]
}
